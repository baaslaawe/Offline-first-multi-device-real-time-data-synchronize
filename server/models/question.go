package models

import (
	"context"
	"time"

	"github.com/moka-a/epodule/factory"

	"github.com/go-xorm/xorm"
)

type Question struct {
	Id        int64     `json:"id"`
	CreatedAt time.Time `json:"createdAt" xorm:"created"`
	UpdatedAt time.Time `json:"updatedAt" xorm:"updated"`
}

func (d *Question) Create(ctx context.Context) (int64, error) {
	return factory.DB(ctx).Insert(d)
}

func (Question) GetById(ctx context.Context, id int64) (*Question, error) {
	var v Question
	if has, err := factory.DB(ctx).ID(id).Get(&v); err != nil {
		return nil, err
	} else if !has {
		return nil, nil
	}
	return &v, nil
}

func (Question) GetAll(ctx context.Context, sortby, order []string, offset, limit int) (totalCount int64, items []Question, err error) {
	queryBuilder := func() xorm.Interface {
		q := factory.DB(ctx)
		if err := setSortOrder(q, sortby, order); err != nil {
			factory.Logger(ctx).Error(err)
		}
		return q
	}

	errc := make(chan error)
	go func() {
		v, err := queryBuilder().Count(&Question{})
		if err != nil {
			errc <- err
			return
		}
		totalCount = v
		errc <- nil
	}()

	go func() {
		if err := queryBuilder().Limit(limit, offset).Find(&items); err != nil {
			errc <- err
			return
		}
		errc <- nil
	}()

	if err := <-errc; err != nil {
		return 0, nil, err
	}
	if err := <-errc; err != nil {
		return 0, nil, err
	}
	return
}

func (d *Question) Update(ctx context.Context) (err error) {
	_, err = factory.DB(ctx).ID(d.Id).Update(d)
	return
}

func (Question) Delete(ctx context.Context, id int64) (err error) {
	_, err = factory.DB(ctx).ID(id).Delete(&Question{})
	return
}

type JsonTime time.Time

func (j JsonTime) format() string {
	return time.Time(j).Format("200601021504")
}

func (j JsonTime) MarshalText() ([]byte, error) {
	return []byte(j.format()), nil
}

func (j JsonTime) MarshalJSON() ([]byte, error) {
	return []byte(`"` + j.format() + `"`), nil
}
